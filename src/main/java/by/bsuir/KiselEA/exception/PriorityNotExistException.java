package by.bsuir.KiselEA.exception;

public class PriorityNotExistException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Приоритет не найдена";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
