package by.bsuir.KiselEA.exception;

public class TaskAlreadyExistException extends BadRequestException {
    private static final String ERROR_MESSAGE = "Задача уже был создана";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
