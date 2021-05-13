package by.bsuir.KiselEA.exception;

public class SprintAlreadyExistException extends BadRequestException {
    private static final String ERROR_MESSAGE = "Спринт уже был создан";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
