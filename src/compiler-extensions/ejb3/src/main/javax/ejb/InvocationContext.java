package javax.ejb;

import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationContext
{
    public abstract Object getBean();

    public abstract Method getMethod();

    public abstract Object[] getParameters();

//    public abstract void setParameters(Object aobj[]);
//
//    public abstract EJBContext getEJBContext();
//
//    public abstract Map getContextData();

    public abstract Object proceed()
        throws Exception;
}