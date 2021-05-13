package by.bsuir.KiselEA.exception;

public class EmployeeNotExistException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Работник не найдена";

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
