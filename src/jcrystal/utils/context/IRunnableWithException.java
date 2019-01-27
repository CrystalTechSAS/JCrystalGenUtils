package jcrystal.utils.context;

public interface IRunnableWithException<T extends Exception> {
	public void run()throws T;
}
