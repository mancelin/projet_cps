package contracts;

public class ContractError extends Error {
	private static final long serialVersionUID = 7213342672775399025L;

	public ContractError(String message) {
		super(message);
	}
}
