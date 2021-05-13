package by.bsuir.KiselEA.exception;

public class ProjectAlreadyExistException extends BadRequestException {
    private static final String ERROR_MESSAGE = "Проект уже был создан";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
