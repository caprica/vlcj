package uk.co.caprica.vlcj.factory;

import com.sun.jna.Pointer;

public interface DialogHandler {

    void displayError(Pointer userData, String title, String text);

    void displayLogin(Pointer userData, DialogId id, String title, String text, String defaultUsername, boolean askStore);

    void displayQuestion(Pointer userData, DialogId id, String title, String text, DialogQuestionType type, String cancel, String action1, String action2);

    void displayProgress(Pointer userData, DialogId id, String title, String text, int indeterminate, float position, String cancel);

    void cancel(Pointer userData, DialogId id);

    void updateProgress(Pointer userData, DialogId id, float position, String text);

}
