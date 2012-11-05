package tatoo.model.conditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;

/**
 * Erzeugt aus Conditions aus einem String. Der String muss in der Infixform
 * gegeben sein und die zu erzeugenden Conditions als Formel beschreiben.
 * 
 * @author mkortz
 */
public class ConditionParser {

    /**
     * Erlaubte Operatoren in umgekehrter Reihenfolge der Wertigkeit
     */
    private static final String operators   = "-+/*<>=";
    /**
     * Regular Expression für Operanden
     */
    private static final String operands    = "([A-Za-z0-9_ ]+\\.?)+";
    /**
     * Klammern welche in dem Term erlaubt sind
     */
    private static final String parenthesis = "()";

    /**
     * Der Wurzelknoten
     */
    private ParseNode           rootEntity;

    public ConditionParser (AbstractEntity rootNode) {
        rootEntity = new ParseNode (rootNode);
        buildParseTree (rootNode);
    }

    private void buildParseTree (AbstractEntity entity) {
        buildParseTree (entity, rootEntity);
        if (entity.hasChilds ()) {
            ;
        }
    }

    private void buildParseTree (AbstractEntity entity, ParseNode parseNode) {

        for (AbstractEntity ae : entity.getChilds ()) {
            if (ae.getType () == EntityType.ROOT || ae.getType () == EntityType.CATEGORY
                            || ae.getType () == EntityType.NODE || ae.getType () == EntityType.UPGRADE) {
                ParseNode newChild = new ParseNode (ae);
                parseNode.addChild (newChild);
                buildParseTree (ae, newChild);
            }
            else {
                if (ae.hasChilds ())
                    buildParseTree (ae, parseNode);
            }
        }

    }

    /**
     * Evaluiert die Infixform der Conditions und erzeugt einen Condition-Baum
     * aus dem String.
     * 
     * @param infix
     * Infixterm mit Conditdions als Operanden.
     * @return der durch den übergebenen String festgelegte Condition-Baum
     */
    @SuppressWarnings ("rawtypes")
    public Condition createCondition (String infix) {
        Condition result = evaluatePostfix (convertToPostfix (infix));
        if (result == null)
            throw new ConditionParseException ();
        return result;
    }

    private ArrayList <String> convertToPostfix (String infixExpr) {
        // whitespaces entfernen und zu array machen
        // String[] terms = toTermArray (infixExpr.replaceAll ("\\s", ""));
        String[] terms = toTermArray (infixExpr);

        Stack <String> stack = new Stack <String> ();
        ArrayList <String> out = new ArrayList <String> ();

        for (String term : terms) {
            if (isOperator (term)) {
                while ( !stack.isEmpty () && !stack.peek ().equals ("(")) {
                    if (operatorGreaterOrEqual (stack.peek ().toCharArray ()[0], term.toCharArray ()[0])) {
                        out.add (stack.pop ());
                    }
                    else {
                        break;
                    }
                }
                stack.push (term);
            }
            else if (term.equals ("(")) {
                stack.push (term);
            }
            else if (term.equals (")")) {
                while ( !stack.isEmpty () && !stack.peek ().equals ("(")) {
                    out.add (stack.pop ());
                }
                if ( !stack.isEmpty ()) {
                    stack.pop ();
                }
            }
            else if (isOperand (term)) {
                out.add (term);
            }
        }
        while ( !stack.empty ()) {
            out.add (stack.pop ());
        }
        return out;
    }

    /**
     * Aus einem Postfix die Condition zusammenbauen
     * 
     * @param postfixExpr
     * @return
     */
    @SuppressWarnings ({"rawtypes", "unchecked"})
    private Condition evaluatePostfix (ArrayList <String> postfixExpr) {
        Stack <Condition> stack = new Stack <Condition> ();
        if (postfixExpr.size () == 0)
            throw new ConditionParseException ();
        for (String term : postfixExpr) {
            if (isOperator (term)) {
                Condition op1;
                Condition op2;
                try {
                    op1 = stack.pop ();
                    op2 = stack.pop ();
                }
                catch (EmptyStackException ese) {
                    throw new ConditionParseException ();
                }
                if (op1 == null || op2 == null)
                    throw new ConditionParseException ();
                CalculatedNumber.Arithmetic arNum = null;
                switch (term.charAt (0)) {
                    case '*':
                        arNum = CalculatedNumber.Arithmetic.MULTIPLY;
                        break;
                    case '/':
                        arNum = CalculatedNumber.Arithmetic.DIVIDE;
                        break;
                    case '+':
                        arNum = CalculatedNumber.Arithmetic.ADD;
                        break;
                    case '-':
                        arNum = CalculatedNumber.Arithmetic.SUBTRACT;
                        break;
                }
                if (arNum != null)
                    stack.push (new CalculatedNumber (op2, op1, arNum));
                else {
                    TrueFalseCondition.Arithmetic arBool = null;
                    switch (term.charAt (0)) {
                        case '=':
                            arBool = TrueFalseCondition.Arithmetic.EQ;
                            break;
                        case '>':
                            arBool = TrueFalseCondition.Arithmetic.GT;
                            break;
                        case '<':
                            arBool = TrueFalseCondition.Arithmetic.LT;
                            break;
                    }
                    if (arBool != null)
                        stack.push (new TrueFalseCondition (op2, op1, arBool));
                }
            }
            else if (isOperand (term)) {
                // Condition suchen bzw. instantiieren und auf den Stack legen.
                // Das ist einfach wenn es sich um eine Zahl handelt:
                if (term.matches ("[0-9]+")) {
                    stack.push (new SimpleNumber (Integer.parseInt (term)));
                }
                // ansonsten muss es sich um den Pfad zu einer Condition
                // handeln.
                else {
                    stack.push (getCondition (term));
                }
            }
        }
        return stack.pop ();
    }

    /**
     * Sucht die Condition aus dem EntityBaum heraus und gibt sie zurück.
     * 
     * @param term
     * Der Term, der den Ort der Condition beschreibt.
     * @return Die Condition.
     */
    @SuppressWarnings ("rawtypes")
    private Condition getCondition (String term) {

        ParseNode tmp_node = rootEntity;

        // zunächst den Term auteilen
        String[] terms = term.split ("\\.");

        // den ersten term prüfen. Es muss sich hier um den rootNode handeln
        if ( !terms[0].equalsIgnoreCase (rootEntity.entity.getName ()))
            throw new ConditionParseException ();
        // TODO: eventuell hier zurückliefern WELCHER Term nicht stimmt?

        for (int i = 1; i < terms.length; i++ ) {

            // zunächst versuchen den Conditiontype zu holen
            ConditionTypes conditionType = null;
            try {
                conditionType = ConditionTypes.valueOf (terms[i].toUpperCase ());
            }
            catch (IllegalArgumentException iae) {}

            // ist der Conditiontype nicht null, kann das entsprechende Attribut
            // geholt werden ...
            if (conditionType != null) {
                return tmp_node.entity.getAttribute (conditionType);
            }
            else {
                // ... ansonsten versuchen den Knoten als Kind des elternknotens
                // zu beziehen:
                ParseNode pn = tmp_node.getChild (terms[i]);
                if (pn == null)
                    // wenn der Knoten leer ist ist der Term falsch
                    throw new ConditionParseException ();
                else
                // ansonsten den neuen tmp_node zuweisen
                tmp_node = pn;
            }
        }
        throw new ConditionParseException ();
    }

    /**
     * Wandelt den in infixform vorliegenden Term in ein Array von Strings um.
     * Die Elemente des Arrays sind die Operatoren, Operanden und Klammern des
     * Terms.
     * 
     * @param infixExpr
     * der Term in Infixform
     * @return das Array aus operanden und Operatoren
     */
    private String[] toTermArray (String infixExpr) {
        ArrayList <String> resultArr = new ArrayList <String> ();
        String resultString = "";
        for (int i = 0; i < infixExpr.length (); i++ ) {
            String c = infixExpr.substring (i, i + 1);
            if (isOperator (c)) {
                if (resultString.length () > 0) {
                    resultArr.add (resultString.trim ());
                    resultString = new String ();
                }
                resultArr.add (c);
            }
            else if (isParenthesis (c)) {
                if (resultString.length () > 0) {
                    resultArr.add (resultString.trim ());
                    resultString = new String ();
                }
                resultArr.add (c);
            }
            else {
                resultString += c;
                // if (isBooleanExpression (resultString)) {
                // resultArr.add (resultString.trim ());
                // resultString = new String ();
                // }

            }
        }
        if (resultString.length () > 0)
            resultArr.add (resultString.trim ());
        return resultArr.toArray (new String[resultArr.size ()]);
    }

    private int getPrecedence (char operator) {
        int ret = 0;
        if (operator == '-' || operator == '+') {
            ret = 1;
        }
        else if (operator == '*' || operator == '/') {
            ret = 2;
        }
        else if (operator == '<' || operator == '>') {
            ret = 3;
        }
        return ret;
    }

    @SuppressWarnings ("rawtypes")
    public String getConditionString (AbstractEntity entity, Condition condition) {
        String resultString = "";
        AbstractEntity owner = condition.getOwnerNode ();

        if (owner != null) {
            if (entity == owner) {
                if (condition instanceof SimpleNumber) {
                    resultString += condition.getValue ().toString ();
                }
                else if (condition instanceof CalculatedCondition) {

                    resultString += buildCalculatedNumberString (entity, (CalculatedCondition) condition);
                }
                else {
                    throw new ConditionParseException ();
                }
            }
            else {
                ConditionTypes type = null;
                for (ConditionTypes t : ConditionTypes.values ())
                    if (owner.getAttribute (t) == condition) {
                        type = t;
                        break;
                    }
                if (type == null)
                    resultString += condition.getValue ().toString ();
                else {
                    resultString += getEntityName (rootEntity, owner);
                    resultString += "." + type.name ();
                }
            }
        }
        else {
            if (condition instanceof CalculatedCondition)
                resultString += buildCalculatedNumberString (entity, (CalculatedCondition) condition);
            else resultString += condition.getValue ().toString ();
        }

        return resultString;
    }

    private String getEntityName (ParseNode searchNode, AbstractEntity entity) {
        if (searchNode.entity == entity)
            return searchNode.entity.getName ();
        else for (ParseNode child : searchNode.getChilds ()) {
            String foundNode = getEntityName (child, entity);
            if (foundNode != null)
                return searchNode.entity.getName () + "." + foundNode;
        }
        return null;
    }

    @SuppressWarnings ("rawtypes")
    private String buildCalculatedNumberString (AbstractEntity entity, CalculatedCondition calcNumber) {

        Condition sourceCondition = calcNumber.getSourceCondition ();
        Condition valueCondition = calcNumber.getValueCondition ();

        String sourceResult;
        String arithResult;
        String valueResult;

        sourceResult = getConditionString (entity, sourceCondition);
        arithResult = " " + calcNumber.getOperator () + " ";
        valueResult = getConditionString (entity, valueCondition);

        // * 1 ausschließen
        if (arithResult.equals (" * ")) {
            if (sourceResult.equals ("1")) {
                sourceResult = "";
                arithResult = "";
            }
            if (valueResult.equals ("1")) {
                valueResult = "";
                arithResult = "";
            }
        }

        if (sourceCondition instanceof SimpleNumber && valueCondition instanceof SimpleNumber)
            return calcNumber.getValue ().toString ();

        // Klammern setzen
        // Prüfen ob im sourceResult ein Operand ist (sonst müssen keine
        // Klammern gesetzt werden
        boolean sourceContainsOperand = false;
        if (sourceResult.length () > 0)
            for (int i = 0; i < operators.length (); i++ )
                if (sourceResult.contains (operators.substring (i, i + 1))) {
                    sourceContainsOperand = true;
                    break;
                }
        boolean valueContainsOperand = false;
        if (valueResult.length () > 0)
            for (int i = 0; i < operators.length (); i++ )
                if (valueResult.contains (operators.substring (i, i + 1))) {
                    valueContainsOperand = true;
                    break;
                }
        if (sourceCondition instanceof CalculatedCondition
                        && getPrecedence (((CalculatedCondition) sourceCondition).getOperator ()) < getPrecedence (calcNumber.getOperator ())
                        && valueResult.length () > 0 && sourceContainsOperand) {
            sourceResult = "(" + sourceResult + ")";
        }

        if (valueCondition instanceof CalculatedCondition
                        && getPrecedence (((CalculatedCondition) valueCondition).getOperator ()) < getPrecedence (calcNumber.getOperator ())
                        && sourceResult.length () > 0 && valueContainsOperand) {
            valueResult = "(" + valueResult + ")";
        }

        return sourceResult + arithResult + valueResult;
    }

    private boolean operatorGreaterOrEqual (char op1, char op2) {
        return getPrecedence (op1) >= getPrecedence (op2);
    }

    private boolean isOperator (String val) {
        return operators.indexOf (val) >= 0 && val.length() > 0;
    }

    private boolean isParenthesis (String val) {
        return parenthesis.indexOf (val) >= 0 && val.length() > 0;
    }

    private boolean isOperand (String val) {
        return val.matches (operands);
    }

    // private boolean isBooleanExpression (String val) {
    // if (val.endsWith (" ")) {
    // String[] boolExpr = booleanExpressions.split (",");
    // for (String expr : boolExpr)
    // if (expr.equalsIgnoreCase (val.trim ()))
    // return true;
    // }
    // return false;
    // }

    private class ParseNode {

        AbstractEntity              entity;

        ParseNode                   parent;

        HashMap <String, ParseNode> childs = new HashMap <String, ParseNode> ();

        public ParseNode (AbstractEntity entity) {
            this.entity = entity;
        }

        public Collection <ParseNode> getChilds () {
            return childs.values ();
        }

        public ParseNode getChild (String s) {
            return childs.get (s.toLowerCase ().replaceAll ("\\s", ""));
        }

        public void addChild (ParseNode node) {
            childs.put (node.entity.getName ().toLowerCase ().replaceAll ("\\s", ""), node);
            node.parent = this;
        }

    }
}
