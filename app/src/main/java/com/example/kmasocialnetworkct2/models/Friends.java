package com.example.kmasocialnetworkct2.models;

public class Friends {
    String friendName,friendId;
    Long friendMsgTimeLast;

    public Friends() {
    }

    public Friends(String friendName, String friendId) {
        this.friendName = friendName;
        this.friendId = friendId;
    }

    public Friends(String friendName, String friendId, Long friendMsgTimeLast) {
        this.friendName = friendName;
        this.friendId = friendId;
        this.friendMsgTimeLast = friendMsgTimeLast;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public Long getFriendMsgTimeLast() {
        return friendMsgTimeLast;
    }

    public void setFriendMsgTimeLast(Long friendMsgTimeLast) {
        this.friendMsgTimeLast = friendMsgTimeLast;
    }
}
