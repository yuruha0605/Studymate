package edu.yonsei.Studymate.Studygroup.exception;

public class GroupFullException extends RuntimeException {
    public GroupFullException(String message) {
        super(message);
    }
}
