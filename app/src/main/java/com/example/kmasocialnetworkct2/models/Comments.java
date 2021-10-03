package com.example.kmasocialnetworkct2.models;

public class Comments {
    String userComment, userIdComment, contentComment, CommentId;
    Long timeCommentPost;

    public Comments(String userComment, String userIdComment, String contentComment, String commentId, Long timeCommentPost) {
        this.userComment = userComment;
        this.userIdComment = userIdComment;
        this.contentComment = contentComment;
        CommentId = commentId;
        this.timeCommentPost = timeCommentPost;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserIdComment() {
        return userIdComment;
    }

    public void setUserIdComment(String userIdComment) {
        this.userIdComment = userIdComment;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public Long getTimeCommentPost() {
        return timeCommentPost;
    }

    public void setTimeCommentPost(Long timeCommentPost) {
        this.timeCommentPost = timeCommentPost;
    }

    public Comments() {
    }
}
