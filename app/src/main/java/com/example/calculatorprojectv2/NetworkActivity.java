package com.example.calculatorprojectv2;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Our WalkieTalkie Activity. This Activity has 4 {@link State}s.
 *
 * <p>{@link State#UNKNOWN}: We cannot do anything while we're in this state. The app is likely in
 * the background.
 *
 * <p>{@link State#DISCOVERING}: Our default state (after we've connected). We constantly listen for
 * a device to advertise near us.
 *
 * <p>{@link State#ADVERTISING}: If a user shakes their device, they enter this state. We advertise
 * our device so that others nearby can discover us.
 *
 * <p>{@link State#CONNECTED}: We've connected to another device. We can now talk to them by holding
 * down the volume keys and speaking into the phone. We'll continue to advertise (if we were already
 * advertising) so that more people can connect to us.
 */
public class NetworkActivity extends ConnectionsActivity implements SensorEventListener {
    /**
     * The connection strategy we'll use for Nearby Connections. In this case, we've decided on
     * P2P_STAR, which is a combination of Bluetooth Classic and WiFi Hotspots.
     */
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    /** Acceleration required to detect a shake. In multiples of Earth's gravity. */
    private static final float SHAKE_THRESHOLD_GRAVITY = 2;

    /**
     * Advertise for 30 seconds before going back to discovering. If a client connects, we'll continue
     * to advertise indefinitely so others can still connect.
     */
    private static final long ADVERTISING_DURATION = 30000;

    /** How long to vibrate the phone when we change states. */
    private static final long VIBRATION_STRENGTH = 500;

    /** Length of state change animations. */
    private static final long ANIMATION_DURATION = 600;

    /**
     * This service id lets us find other nearby devices that are interested in the same thing. Our
     * sample does exactly one thing, so we hardcode the ID.
     */
    private static final String SERVICE_ID =
            "com.google.location.nearby.apps.walkietalkie.manual.SERVICE_ID";

    /**
     * The state of the app. As the app changes states, the UI will update and advertising/discovery
     * will start/stop.
     */
    private State mState = State.UNKNOWN;

    /** A random UID used as this device's endpoint name. */
    private String mName;

    /** Displays the previous state during animation transitions. */
    private TextView mPreviousStateView;

    /** Displays the current state. */
    private TextView mCurrentStateView;

    /** Moves to calculator activity */
    private Button bNext;

    /** An animator that controls the animation from previous state to current state. */
    @Nullable
    private Animator mCurrentAnimator;

    /** A running log of debug messages. Only visible when DEBUG=true. */
    private TextView mDebugLogView;

    /** The SensorManager gives us access to sensors on the device. */
    private SensorManager mSensorManager;

    /** The accelerometer sensor allows us to detect device movement for shake-to-advertise. */
    private Sensor mAccelerometer;

    /** A running display of the endpoints that we are connected to. **/
    private TextView mEndpointsLogView;

    /**
     * A Handler that allows us to post back on to the UI thread. We use this to resume discovery
     * after an uneventful bout of advertising.
     */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    /** Starts discovery. Used in a postDelayed manor with {@link #mUiHandler}. */
    private final Runnable mDiscoverRunnable = () -> setState(State.DISCOVERING);

    /** Gets the information from the other things */
    private final ArrayList<ArrayList<String>> endpointIdToResponse =
            new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>()));

    /** ViewModel to communicate with fragment */
    private NetworkViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mPreviousStateView = findViewById(R.id.previous_state);
        mCurrentStateView = findViewById(R.id.current_state);

        mDebugLogView = findViewById(R.id.debug_log);
        mDebugLogView.setMovementMethod(new ScrollingMovementMethod());

        mName = generateRandomName();

        ((TextView) findViewById(R.id.name)).setText(mName);

        bNext = findViewById(R.id.start_button);
        bNext.setOnClickListener(view -> {
            String s = "GAME_STARTED";
            send(Payload.fromBytes(s.getBytes(UTF_8)));
            initializeCalculatorFragment();
        });

        mEndpointsLogView = findViewById(R.id.endpoints_log);

        viewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        viewModel.reset();
        viewModel.getCurrentLevel().observe(this, level -> {
            if(raceBars.size() != 0) {
                raceBars.get(0).incrementProgressBy(10);
            }
            send(Payload.fromBytes(("LEVEL:" + level).getBytes(UTF_8)));
            if(level > 10) {
                String s = "WINNER IS:" + getName();
                switchToEndFragment();
                send(Payload.fromBytes(s.getBytes(UTF_8)));
            }
        });

        viewModel.getCompanionLevels().observe(this, levels -> {
            for (int i = 0; i < levels.size(); i++) {
                raceBars.get(i).setProgress(10 * levels.get(i));
                endpointIdToResponse.get(1).set(i, levels.get(i) + "");
            }
            System.out.println(endpointIdToResponse.get(0) + "\n" +  endpointIdToResponse.get(1));
        });
    }

    public void initializeCalculatorFragment() {
        findViewById(R.id.lobby_layout).setVisibility(View.GONE);

        // Everything is set to 0 for multiplayer
        Bundle args = new Bundle();
        args.putString("Points", "0");
        args.putString("isDoublePointsEnabled", "false");
        args.putString("NumDouble", "0");
        args.putString("NumFreeze", "0");
        args.putString("NumClick", "0");
        args.putString("useNetworkEndFragment", "true");

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.calculator_fragment, CalculatorFragment.class, args)
                .commit();

        findViewById(R.id.calculator_fragment).setVisibility(View.VISIBLE);
        findViewById(R.id.race_layout).setVisibility(View.VISIBLE);
        addRaceBars();
    }

    public void switchToEndFragment() {
        Bundle args = new Bundle();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.calculator_fragment, NetworkEndFragment.class, args)
                .commit();
    }

    private ArrayList<ProgressBar> raceBars = new ArrayList<>();

    public void addRaceBars() {
        // Add one for each endpoint and one for your own
        for (int i = -1; i < getConnectedEndpoints().size(); i++) {
            ProgressBar progressBar = new ProgressBar(this, null,
                    android.R.attr.progressBarStyleHorizontal);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setId(ViewCompat.generateViewId());
            progressBar.setScaleY(8);
            raceBars.add(progressBar);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 8, 0, 8);
            LinearLayout linearLayout = findViewById(R.id.race_layout);
            linearLayout.addView(progressBar, params);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mState == State.CONNECTED) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        setState(State.DISCOVERING);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);

        if(getConnectedEndpoints().isEmpty()) {
            setState(State.UNKNOWN);
        }

        mUiHandler.removeCallbacksAndMessages(null);

        if (mCurrentAnimator != null && mCurrentAnimator.isRunning()) {
            mCurrentAnimator.cancel();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (getState() == State.CONNECTED || getState() == State.ADVERTISING) {
            setState(State.DISCOVERING);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onEndpointDiscovered(Endpoint endpoint) {
        // We found an advertiser!
        if (!isConnecting()) {
            connectToEndpoint(endpoint);
            send(Payload.fromBytes(("ID:" + endpoint.getId()).getBytes(UTF_8)));
        }
    }

    @Override
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        // A connection to another device has been initiated! We'll accept the connection immediately.
        acceptConnection(endpoint);
        endpointIdToResponse.get(0).add(endpoint.getId());
        endpointIdToResponse.get(1).add("");
        String s = "ID: " + endpointIdToResponse.get(0).get(endpointIdToResponse.get(0).size() - 1);
        System.out.println(s);

        send(Payload.fromBytes(s.getBytes(UTF_8)));
    }

    @Override
    protected void onEndpointConnected(Endpoint endpoint) {
        getConnectedEndpoints().add(endpoint);
        updateEndpointLogs();
        Toast.makeText(
                this, getString(R.string.toast_connected, endpoint.getName()), Toast.LENGTH_SHORT)
                .show();
        setState(State.CONNECTED);
    }

    @Override
    protected void onEndpointDisconnected(Endpoint endpoint) {
        Toast.makeText(
                this, getString(R.string.toast_disconnected, endpoint.getName()), Toast.LENGTH_SHORT)
                .show();
        getConnectedEndpoints().remove(endpoint);
        updateEndpointLogs();
        // If we lost all our endpoints, then we should reset the state of our app and go back
        // to our initial state (discovering).
        if (getConnectedEndpoints().isEmpty()) {
            setState(State.DISCOVERING);
        }
    }

    @Override
    protected void onConnectionFailed(Endpoint endpoint) {
        // Let's try someone else.
        if (getState() == State.DISCOVERING && !getDiscoveredEndpoints().isEmpty()) {
            connectToEndpoint(pickRandomElem(getDiscoveredEndpoints()));
        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    private void setState(State state) {
        if (mState == state) {
            logW("State set to " + state + " but already in that state");
            return;
        }

        logD("State set to " + state);
        State oldState = mState;
        mState = state;
        onStateChanged(oldState, state);
    }

    /** @return The current state. */
    private State getState() {
        return mState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(State oldState, State newState) {
        if (mCurrentAnimator != null && mCurrentAnimator.isRunning()) {
            mCurrentAnimator.cancel();
        }

        // Update Nearby Connections to the new state.
        switch (newState) {
            case DISCOVERING:
                if (isAdvertising()) {
                    stopAdvertising();
                }
                disconnectFromAllEndpoints();
                startDiscovering();
                break;
            case ADVERTISING:
                if (isDiscovering()) {
                    stopDiscovering();
                }
                disconnectFromAllEndpoints();
                startAdvertising();
                break;
            case CONNECTED:
                if (isDiscovering()) {
                    stopDiscovering();
                } else if (isAdvertising()) {
                    // Continue to advertise, so others can still connect,
                    // but clear the discover runnable.
                    removeCallbacks(mDiscoverRunnable);
                }
                break;
            case UNKNOWN:
                stopAllEndpoints();
                break;
            default:
                // no-op
                break;
        }

        // Update the UI.
        switch (oldState) {
            case UNKNOWN:
                // Unknown is our initial state. Whatever state we move to,
                // we're transitioning forwards.
                transitionForward(oldState, newState);
                break;
            case DISCOVERING:
                switch (newState) {
                    case UNKNOWN:
                        transitionBackward(oldState, newState);
                        break;
                    case ADVERTISING:
                    case CONNECTED:
                        transitionForward(oldState, newState);
                        break;
                    default:
                        // no-op
                        break;
                }
                break;
            case ADVERTISING:
                switch (newState) {
                    case UNKNOWN:
                    case DISCOVERING:
                        transitionBackward(oldState, newState);
                        break;
                    case CONNECTED:
                        transitionForward(oldState, newState);
                        break;
                    default:
                        // no-op
                        break;
                }
                break;
            case CONNECTED:
                // Connected is our final state. Whatever new state we move to,
                // we're transitioning backwards.
                transitionBackward(oldState, newState);
                break;
            default:
                // no-op
                break;
        }
    }

    /** Transitions from the old state to the new state with an animation implying moving forward. */
    @UiThread
    private void transitionForward(State oldState, final State newState) {
        mPreviousStateView.setVisibility(View.VISIBLE);
        mCurrentStateView.setVisibility(View.VISIBLE);

        updateTextView(mPreviousStateView, oldState);
        updateTextView(mCurrentStateView, newState);

        if (ViewCompat.isLaidOut(mCurrentStateView)) {
            mCurrentAnimator = createAnimator(false /* reverse */);
            mCurrentAnimator.addListener(
                    new AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            updateTextView(mCurrentStateView, newState);
                        }
                    });
            mCurrentAnimator.start();
        }
    }

    /** Transitions from the old state to the new state with an animation implying moving backward. */
    @UiThread
    private void transitionBackward(State oldState, final State newState) {
        mPreviousStateView.setVisibility(View.VISIBLE);
        mCurrentStateView.setVisibility(View.VISIBLE);

        updateTextView(mCurrentStateView, oldState);
        updateTextView(mPreviousStateView, newState);

        if (ViewCompat.isLaidOut(mCurrentStateView)) {
            mCurrentAnimator = createAnimator(true /* reverse */);
            mCurrentAnimator.addListener(
                    new AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            updateTextView(mCurrentStateView, newState);
                        }
                    });
            mCurrentAnimator.start();
        }
    }

    @NonNull
    private Animator createAnimator(boolean reverse) {
        Animator animator;
        int cx = mCurrentStateView.getMeasuredWidth() / 2;
        int cy = mCurrentStateView.getMeasuredHeight() / 2;
        int initialRadius = 0;
        int finalRadius = Math.max(mCurrentStateView.getWidth(), mCurrentStateView.getHeight());
        if (reverse) {
            int temp = initialRadius;
            initialRadius = finalRadius;
            finalRadius = temp;
        }
        animator =
                ViewAnimationUtils.createCircularReveal(
                        mCurrentStateView, cx, cy, initialRadius, finalRadius);
        animator.addListener(
                new AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animator) {
                        mPreviousStateView.setVisibility(View.GONE);
                        mCurrentStateView.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mPreviousStateView.setVisibility(View.GONE);
                        mCurrentStateView.setAlpha(1);
                    }
                });
        animator.setDuration(ANIMATION_DURATION);
        return animator;
    }

    /** Updates the {@link TextView} with the correct color/text for the given {@link State}. */
    @UiThread
    private void updateTextView(TextView textView, State state) {
        switch (state) {
            case DISCOVERING:
                textView.setBackgroundResource(R.color.state_discovering);
                textView.setText(R.string.status_discovering);
                break;
            case ADVERTISING:
                textView.setBackgroundResource(R.color.state_advertising);
                textView.setText(R.string.status_advertising);
                break;
            case CONNECTED:
                textView.setBackgroundResource(R.color.state_connected);
                textView.setText(R.string.status_connected);
                break;
            default:
                textView.setBackgroundResource(R.color.state_unknown);
                textView.setText(R.string.status_unknown);
                break;
        }
    }

    /** The device has moved. We need to decide if it was intentional or not. */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY && getState() == State.DISCOVERING) {
            logD("Device shaken");
            vibrate();
            setState(State.ADVERTISING);
            postDelayed(mDiscoverRunnable);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /** Vibrates the phone. */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (hasPermissions(this, Manifest.permission.VIBRATE) && vibrator.hasVibrator()) {
            vibrator.vibrate(VIBRATION_STRENGTH);
        }
    }

    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)} */
    @Override
    protected void onReceive(Endpoint endpoint, Payload payload) {
        String input = new String(payload.asBytes(), UTF_8);
        int index = endpointIdToResponse.get(0).indexOf(endpoint.getId());
        endpointIdToResponse.get(1).set(index, input);
        logD("CONNECTION RECIEVED " + endpoint.getId());
        interpretRecievedInfo(input, endpoint);
    }

    public void interpretRecievedInfo(String input, Endpoint endpoint) {
        if(input.equals("GAME_STARTED")) {
            initializeCalculatorFragment();
        } else if (input.contains("LEVEL:")) {
            String input2 = input.substring(input.indexOf(":") + 1);
            for (int i = 0; i < endpointIdToResponse.get(0).size(); i++) {
                if (endpointIdToResponse.get(0).get(i).equals(endpoint.getId())) {
                    endpointIdToResponse.get(1).set(i, input2);
                    raceBars.get(i).incrementProgressBy(10);
                }
            }
            // viewModel.setCurrentLevel(Integer.parseInt(input2));
            System.out.println(input2 + " " + endpoint.getName());
            viewModel.setCompanionLevels(Integer.parseInt(input2), endpoint.getName());
        } else if (input.contains("ID:")) {
            String input2 = input.substring(input.indexOf(":") + 1);
            connectToEndpoint(input2);
        } else if (input.contains("WINNER IS:")) {
            // String input2 = input.substring(input.indexOf(":") + 1);
            switchToEndFragment();
        } else {
            Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
        }
    }

    /** {@see ConnectionsActivity#getRequiredPermissions()} */
    @Override
    protected String[] getRequiredPermissions() {
        return join(
                super.getRequiredPermissions());
    }

    /** Joins 2 arrays together. */
    private static String[] join(String[] a, String... b) {
        String[] join = new String[a.length + b.length];
        System.arraycopy(a, 0, join, 0, a.length);
        System.arraycopy(b, 0, join, a.length, b.length);
        return join;
    }

    /**
     * Queries the phone's contacts for their own profile, and returns their name. Used when
     * connecting to another device.
     */
    @Override
    protected String getName() {
        return mName;
    }

    /** {@see ConnectionsActivity#getServiceId()} */
    @Override
    public String getServiceId() {
        return SERVICE_ID;
    }

    /** {@see ConnectionsActivity#getStrategy()} */
    @Override
    public Strategy getStrategy() {
        return STRATEGY;
    }

    /** {@see Handler#post()} */
    protected void post(Runnable r) {
        mUiHandler.post(r);
    }

    /** {@see Handler#postDelayed(Runnable, long)} */
    protected void postDelayed(Runnable r) {
        mUiHandler.postDelayed(r, NetworkActivity.ADVERTISING_DURATION);
    }

    /** {@see Handler#removeCallbacks(Runnable)} */
    protected void removeCallbacks(Runnable r) {
        mUiHandler.removeCallbacks(r);
    }

    @Override
    protected void logV(String msg) {
        super.logV(msg);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_verbose)));
    }

    @Override
    protected void logD(String msg) {
        super.logD(msg);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_debug)));
    }

    @Override
    protected void logW(String msg) {
        super.logW(msg);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_warning)));
    }

    @Override
    protected void logW(String msg, Throwable e) {
        super.logW(msg, e);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_warning)));
    }

    @Override
    protected void logE(String msg, Throwable e) {
        super.logE(msg, e);
        appendToLogs(toColor(msg, getResources().getColor(R.color.log_error)));
    }

    private void appendToLogs(CharSequence msg) {
        mDebugLogView.append("\n");
        mDebugLogView.append(DateFormat.format("hh:mm", System.currentTimeMillis()) + ": ");
        mDebugLogView.append(msg);
    }

    private void updateEndpointLogs() {
        StringBuilder logText = new StringBuilder().append("Connected Device IDs:\n");
        for (Endpoint e:getConnectedEndpoints()) {
            logText.append(e.getName()).append("\n");
        }
        logText.trimToSize();
        mEndpointsLogView.setText(logText.toString());
    }

    private static CharSequence toColor(String msg, int color) {
        SpannableString spannable = new SpannableString(msg);
        spannable.setSpan(new ForegroundColorSpan(color), 0, msg.length(), 0);
        return spannable;
    }

    private static String generateRandomName() {
        StringBuilder name = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            name.append(random.nextInt(10));
        }
        return name.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T> T pickRandomElem(Collection<T> collection) {
        return (T) collection.toArray()[new Random().nextInt(collection.size())];
    }

    /**
     * Provides an implementation of Animator.AnimatorListener so that we only have to override the
     * method(s) we're interested in.
     */
    private abstract static class AnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {}

        @Override
        public void onAnimationEnd(Animator animator) {}

        @Override
        public void onAnimationCancel(Animator animator) {}

        @Override
        public void onAnimationRepeat(Animator animator) {}
    }

    /** States that the UI goes through. */
    public enum State {
        UNKNOWN,
        DISCOVERING,
        ADVERTISING,
        CONNECTED
    }
}
