package in.pratanumandal.unique4j;

import java.util.function.Consumer;

public class ComposableUnique4j extends Unique4j {

    private final MessageHandler MESSAGE_HANDLER;
    private final Consumer<Throwable> EXCEPTION_HANDLER;
    private final Runnable EXIT_HANDLER;

    public ComposableUnique4j(String APP_ID, MessageHandler MESSAGE_HANDLER) {
        super(APP_ID);
        this.MESSAGE_HANDLER = MESSAGE_HANDLER;
        this.EXCEPTION_HANDLER = null;
        this.EXIT_HANDLER = null;
    }

    public ComposableUnique4j(String APP_ID,
                              MessageHandler MESSAGE_HANDLER,
                              Consumer<Throwable> EXCEPTION_HANDLER,
                              Runnable EXIT_HANDLER) {
        super(APP_ID);
        this.MESSAGE_HANDLER = MESSAGE_HANDLER;
        this.EXCEPTION_HANDLER = EXCEPTION_HANDLER;
        this.EXIT_HANDLER = EXIT_HANDLER;
    }

    public ComposableUnique4j(String APP_ID,
                              IpcFactory IPC_FACTORY,
                              MessageHandler MESSAGE_HANDLER,
                              Consumer<Throwable> EXCEPTION_HANDLER,
                              Runnable EXIT_HANDLER) {
        super(APP_ID, false, IPC_FACTORY);
        this.MESSAGE_HANDLER = MESSAGE_HANDLER;
        this.EXCEPTION_HANDLER = EXCEPTION_HANDLER;
        this.EXIT_HANDLER = EXIT_HANDLER;
    }

    @Override
    protected void receiveMessage(String message) {
        MESSAGE_HANDLER.receiveMessage(message);
    }

    @Override
    protected String sendMessage() {
        return MESSAGE_HANDLER.sendMessage();
    }

    @Override
    protected void handleException(Exception exception) {
        if(EXCEPTION_HANDLER != null)
            EXCEPTION_HANDLER.accept(exception);
        else
            super.handleException(exception);
    }

    @Override
    protected void beforeExit() {
        if(EXIT_HANDLER != null)
            EXIT_HANDLER.run();
        else
            super.beforeExit();
    }

    public interface MessageHandler {

        void receiveMessage(String message);

        String sendMessage();
    }
}
