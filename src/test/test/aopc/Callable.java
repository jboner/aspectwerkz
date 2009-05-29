package test.aopc;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public interface Callable {
    public String getLogString();

    public void log(String s);

    public void debug();

    public void methodAround();

    public void methodPre();

    public void methodPost();
}