package by.bsuir.KiselEA.exception;

public class TaskNotExistException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Задача не найдена";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
