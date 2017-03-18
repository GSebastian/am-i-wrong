package studio.roboto.hack24.questions;

public interface OnQuestionAddedListener {

    void questionAdded(String questionId);

    void questionAddFailed();

}
