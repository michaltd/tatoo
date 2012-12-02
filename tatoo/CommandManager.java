package tatoo;

import java.util.ArrayList;

import tatoo.commands.ActionNotAllowedException;
import tatoo.commands.EmptyCommandExeption;

public class CommandManager {

    private CommandNode[] firstNode;
    private CommandNode[] lastSavedNode;
    private CommandNode[] lastExecutedNode;
    private CommandNode   newAddedCommand;
    private int           activeView;
    private boolean       redoAllowed = false;
    private boolean       isInAction  = false;

    public static enum View {
        ARMYLIST,
        ARMYBUILDER;
    }

    public CommandManager () {
        firstNode = new CommandNode[View.values ().length];
        lastSavedNode = new CommandNode[View.values ().length];
        lastExecutedNode = new CommandNode[View.values ().length];
        newAddedCommand = null;
        activeView = 0;
    }

    public void execute (Command command) {
        if ( !isInAction) {
            isInAction = true;
            if (newAddedCommand != null) {
                isInAction = false;
                throw new ActionNotAllowedException ();
            }

            CommandNode newNode = new CommandNode (command);
            executeCommandNode (newNode);
            isInAction = false;
        }

    }

    private void executeCommandNode (CommandNode newNode) {

        if (newAddedCommand != null) {
            isInAction = false;
            throw new ActionNotAllowedException ();
        }

        if (lastExecutedNode[activeView] != null) {
            if (lastExecutedNode[activeView].next != null)
                // lastExecutedNode next wegschmeissen (bzw. vom gc
                // wegschmeissen lassen :P)
                lastExecutedNode[activeView].next.previous = null;
            lastExecutedNode[activeView].next = newNode;
            newNode.previous = lastExecutedNode[activeView];
        }
        else if (firstNode[activeView] != newNode)
            firstNode[activeView] = newNode;

        lastExecutedNode[activeView] = newNode;
        try {
            lastExecutedNode[activeView].execute ();
        }
        finally {
            isInAction = false;
        }
        redoAllowed = false;
    }

    public void undo () {

        if ( !isInAction) {
            isInAction = true;
            if (newAddedCommand != null) {
                isInAction = false;
                throw new ActionNotAllowedException ();
            }
            if (lastExecutedNode[activeView] != null) {
                try {
                    lastExecutedNode[activeView].undo ();
                }
                finally {
                    isInAction = false;
                }
                lastExecutedNode[activeView] = lastExecutedNode[activeView].previous;
                redoAllowed = true;
            }
            isInAction = false;
        }
    }

    public void redo () {

        if ( !isInAction) {
            isInAction = true;
            if (newAddedCommand != null) {
                isInAction = false;
                throw new ActionNotAllowedException ();
            }
            if (lastExecutedNode[activeView] == null)
                lastExecutedNode[activeView] = firstNode[activeView];
            else if (lastExecutedNode[activeView].next != null)
                lastExecutedNode[activeView] = lastExecutedNode[activeView].next;
            else redoAllowed = false;

            if (lastExecutedNode[activeView] != null && redoAllowed)
                try {
                    lastExecutedNode[activeView].redo ();
                }
                finally {
                    isInAction = false;
                }
            isInAction = false;
        }

    }

    public void add (Command cmd) {
        if ( !isInAction) {
            isInAction = true;
            // noch kein neues unausgeführtes Kommando vorhanden? Anlegen!
            if (newAddedCommand == null)
                newAddedCommand = new CommandNode (cmd);
            else
            // ansonsten Kommando hinzufügen
            newAddedCommand.add (cmd);
            isInAction = false;
        }

    }

    public void execute () {
        if ( !isInAction) {
            isInAction = true;

            if (newAddedCommand == null) {
                isInAction = false;
                throw new EmptyCommandExeption ();
            }

            CommandNode tmpCmd = newAddedCommand;
            newAddedCommand = null;
            try {
                executeCommandNode (tmpCmd);
            }
            catch (ActionNotAllowedException enae) {
                newAddedCommand = tmpCmd;
                isInAction = false;
                throw enae;
            }
            isInAction = false;
        }

    }

    public void write () {

        if (firstNode[activeView] != null) {

            boolean lastNodeGreaterThanSavedNode = false;
            if (lastExecutedNode[activeView] != null)
                if (lastSavedNode[activeView] == null)
                    lastNodeGreaterThanSavedNode = true;
                else {
                    CommandNode tmpNode = lastExecutedNode[activeView].previous;
                    while (tmpNode != null && !lastNodeGreaterThanSavedNode)
                        if (tmpNode != lastSavedNode[activeView])
                            tmpNode = tmpNode.previous;
                        else lastNodeGreaterThanSavedNode = true;
                }

            if (lastNodeGreaterThanSavedNode)
                while (lastSavedNode[activeView] != lastExecutedNode[activeView]) {
                    if (lastSavedNode[activeView] == null)
                        lastSavedNode[activeView] = firstNode[activeView];
                    else lastSavedNode[activeView] = lastSavedNode[activeView].next;
                    lastSavedNode[activeView].writeObject ();
                }
            else {
                // wenn lastSavedNode == null ist dann MUSS lastExecutedNode
                // auch null sein wegen !lastNodeGreaterThanSavedNode. In dem
                // Fall nichts tun.
                while (lastSavedNode[activeView] != lastExecutedNode[activeView] && lastSavedNode[activeView] != null) {
                    lastSavedNode[activeView].deleteObject ();
                    lastSavedNode[activeView] = lastSavedNode[activeView].previous;
                }
            }

        }
    }

    public void setActiveView (View view) {
        activeView = view.ordinal ();
    }

    /**
     * Knoten für eine doppelt verkettete Liste von Commands.
     * 
     * @author mkortz
     */
    private class CommandNode implements Command {

        private ArrayList <Command> cmd = new ArrayList <Command> ();

        private CommandNode         next;
        private CommandNode         previous;

        public CommandNode (Command command) {
            cmd.add (command);
        }

        public void add (Command command) {
            cmd.add (command);
        }

        @Override
        public void execute () {
            for (int i = 0; i < cmd.size (); i++ )
                cmd.get (i).execute ();
        }

        @Override
        public void undo () {
            for (int i = cmd.size () - 1; i >= 0; i-- )
                cmd.get (i).undo ();
        }

        @Override
        public void redo () {
            for (int i = 0; i < cmd.size (); i++ )
                cmd.get (i).redo ();
        }

        @Override
        public void writeObject () {
            for (Command localCmd : cmd)
                localCmd.writeObject ();
        }

        @Override
        public void deleteObject () {
            for (Command localCmd : cmd)
                localCmd.deleteObject ();
        }

    }

}
