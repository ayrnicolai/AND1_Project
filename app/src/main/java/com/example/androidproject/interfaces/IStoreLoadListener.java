package com.example.androidproject.interfaces;

import com.example.androidproject.models.Room;

import java.util.List;

public interface IStoreLoadListener {
    void onBranchLoadSucces(List<Room> RoomList);
    void onBranchLoadFailed(String message);
}
