package net.lwbuchanan.lox;

import net.lwbuchanan.lox.Expr.Binary;
import net.lwbuchanan.lox.Expr.Grouping;
import net.lwbuchanan.lox.Expr.Literal;
import net.lwbuchanan.lox.Expr.Unary;

class Interpreter implements Expr.Visitor<Object> {

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);

            case GREATER:
                return (double)left > (double)right;
            case GREATER_EQUAL:
                return (double)left >= (double)right;
            case LESS:
                return (double)left < (double)right;
            case LESS_EQUAL:
                return (double)left <= (double)right;

            case MINUS:
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double)
                    return (double)left + (double)right;
                if (left instanceof String && right instanceof String)
                    return (String)left + (String) right;
                break;
            case SLASH:
                return (double)left / (double)right;
            case STAR:
                return (double)left * (double)right;
            default:
                break;
        }

        // Should not be reachable
        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                return -(double)right;
            default:
                break;
        }

        // Should not be reachable
        return null;
    }
    
}
