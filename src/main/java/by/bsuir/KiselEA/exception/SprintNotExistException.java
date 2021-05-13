package by.bsuir.KiselEA.exception;

public class SprintNotExistException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Спринт не найден";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
