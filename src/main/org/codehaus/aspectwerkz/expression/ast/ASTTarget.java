/* Generated By:JJTree: Do not edit this line. ASTTarget.java */

package org.codehaus.aspectwerkz.expression.ast;

import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.SubtypePatternType;
import org.codehaus.aspectwerkz.expression.regexp.TypePattern;

public class ASTTarget extends SimpleNode {

  private String m_identifier;

  public ASTTarget(int id) {
    super(id);
  }

  public ASTTarget(ExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  public void setIdentifier(String identifier) {
      m_identifier = identifier;
  }

    public String getIdentifier() {
        return m_identifier;
    }

    public String getBoundedType(ExpressionInfo info) {
        // fast check if it seems to be a bounded type
        if (m_identifier.indexOf(".") < 0) {
            String boundedType = info.getArgumentType(m_identifier);
            if (boundedType != null) {
                return boundedType;
            }
        }
        return m_identifier;
    }
}