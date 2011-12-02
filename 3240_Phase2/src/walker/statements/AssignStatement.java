package walker.statements;

import java.util.Map;

/**
 *
 * @author taylor
 */
public abstract class AssignStatement implements StatementExecutor{
    protected Map<String, Object> idMap;
    
    public AssignStatement(Map<String, Object> idMap) {
        this.idMap = idMap;
    }
}
