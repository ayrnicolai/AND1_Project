package com.example.androidproject.interfaces;

import java.util.List;

public interface IAllRoamLoadListener {
    void onAllRoomLoadSucces(List<String> areaNameList);
    void onAllRoomLoadFailed(String message);
}
