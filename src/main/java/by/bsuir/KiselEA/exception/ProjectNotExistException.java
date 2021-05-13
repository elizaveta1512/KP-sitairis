package by.bsuir.KiselEA.exception;

public class ProjectNotExistException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Проект не найден";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
