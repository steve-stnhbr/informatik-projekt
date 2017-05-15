package matrizen.core.event;

import java.lang.reflect.ParameterizedType;

public abstract class EventListener<E extends Event> {

	public abstract void ausfuehren(E event);

	@SuppressWarnings("unchecked")
	public Class<E> getType() throws Exception {
		ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<E>) superclass.getActualTypeArguments()[0];
	}
}
