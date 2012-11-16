package tatoo;

import tatoo.db.DBFactory;

public class CommandManager {

    private CommandNode firstNode;
    private CommandNode lastSavedNode;
    private CommandNode lastExecutedNode;

    public void execute (AbstractCommand command) {

        CommandNode newNode = new CommandNode (command);

        if (lastExecutedNode != null) {
            if (lastExecutedNode.next != null)
                // lastExecutedNode next wegschmeissen:
                lastExecutedNode.next.previous = null;
            lastExecutedNode.next = newNode;
            newNode.previous = lastExecutedNode;
        }
        else if (firstNode != newNode)
            firstNode = newNode;

        lastExecutedNode = newNode;
        lastExecutedNode.execute ();
    }

    public void undo () {
        if (lastExecutedNode != null) {
            lastExecutedNode.undo ();
            lastExecutedNode = lastExecutedNode.previous;
        }
    }

    public void redo () {

        if (lastExecutedNode == null)
            lastExecutedNode = firstNode;
        else lastExecutedNode = lastExecutedNode.next;

        if (lastExecutedNode != null)
            lastExecutedNode.redo ();
    }

    public void write () {

        if (firstNode != null) {

            boolean lastNodeGreaterThanSavedNode = false;
            if (lastExecutedNode != null)
                if (lastSavedNode == null)
                    lastNodeGreaterThanSavedNode = true;
                else {
                    CommandNode tmpNode = lastExecutedNode.previous;
                    while (tmpNode != null && !lastNodeGreaterThanSavedNode)
                        if (tmpNode != lastSavedNode)
                            tmpNode = tmpNode.previous;
                        else lastNodeGreaterThanSavedNode = true;
                }

            if (lastNodeGreaterThanSavedNode)
                while (lastSavedNode != lastExecutedNode) {
                    if (lastSavedNode == null)
                        lastSavedNode = firstNode;
                    else lastSavedNode = lastSavedNode.next;
                    DBFactory.getInstance ().write (lastSavedNode.cmd);
                }
            else {
                // wenn lastSavedNode == null ist dann MUSS lastExecutedNode auch null
                // sein wegen !lastNodeGreaterThanSavedNode. In dem Fall nichts
                // tun.
                while (lastSavedNode != lastExecutedNode && lastSavedNode != null) {
                    DBFactory.getInstance ().write (lastSavedNode.cmd);
                    lastSavedNode = lastSavedNode.previous;
                }
            }

        }
    }

    private class CommandNode implements Command {

        private AbstractCommand     cmd;

        private CommandNode next;
        private CommandNode previous;

        public CommandNode (AbstractCommand command) {
            cmd = command;
        }

        @Override
        public void execute () {
            cmd.execute ();
        }

        @Override
        public void undo () {
            cmd.undo ();
        }

        @Override
        public void redo () {
            cmd.redo ();
        }

    }

}
