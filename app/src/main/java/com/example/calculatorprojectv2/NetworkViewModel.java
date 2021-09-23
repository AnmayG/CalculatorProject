package com.example.calculatorprojectv2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class NetworkViewModel extends ViewModel {
    private final MutableLiveData<Integer> currentLevel = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<Integer>> companionLevels = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<String>> companionNames = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> points = new MutableLiveData<>(0);
    private final MutableLiveData<String> leader = new MutableLiveData<>("");

    public MutableLiveData<Integer> getCurrentLevel() {
        return currentLevel;
    }

    public MutableLiveData<Integer> getPoints() {
        return points;
    }

    public MutableLiveData<String> getLeader() {
        return leader;
    }

    public MutableLiveData<ArrayList<Integer>> getCompanionLevels() {
        return companionLevels;
    }

    public MutableLiveData<ArrayList<String>> getCompanionNames() {
        return companionNames;
    }

    public void setCurrentLevel(int level) {
        currentLevel.setValue(level);
    }

    public void setPoints(int point) {
        points.setValue(point);
    }

    public void setLeader(String lead) {
        leader.setValue(lead);
    }

    public void setCompanionLevels(ArrayList<Integer> levels) {
        companionLevels.setValue(levels);
    }

    public void setCompanionLevels(int level, int index) {
        equalSizeArray();
        Objects.requireNonNull(companionLevels.getValue()).set(index, level);
        int max = 0;
        for (int i = 0; i < companionLevels.getValue().size(); i++) {
            if(companionLevels.getValue().get(i) > companionLevels.getValue().get(max)) {
                max = i;
            }
        }
        setLeader(Objects.requireNonNull(companionNames.getValue()).get(max));
    }

    public void equalSizeArray() {
        while(Objects.requireNonNull(companionLevels.getValue()).size() <
                Objects.requireNonNull(companionNames.getValue()).size()) {
            companionLevels.getValue().add(0);
        }
    }

    public void setCompanionLevels(int level, String name) {
        int index = Objects.requireNonNull(companionNames.getValue()).indexOf(name);
        if(index == -1) {
            companionNames.getValue().add(name);
            index = companionNames.getValue().size() - 1;
        }
        setCompanionLevels(level, index);
    }

    public void setCompanionNames(ArrayList<String> names) {
        companionNames.setValue(names);
    }

    public void setCompanionNames(String name, int index) {
        Objects.requireNonNull(companionNames.getValue()).set(index, name);
    }

    public void reset() {
        this.setCurrentLevel(0);
        this.setPoints(0);
        this.setLeader("");
        this.setCompanionLevels(new ArrayList<>());
        this.setCompanionNames(new ArrayList<>());
    }
}
