package w094j.ctrl8.database.config;
//@ A0112092W

/**
* The config of the task manager 
* It is currently a stub
*
*/
public class TaskManagerConfig implements IConfig{
    
    
    private AliasConfig alias;
    
    /**
     * Default constructor for task manager
     */
    public TaskManagerConfig(){
        this.alias = new AliasConfig();
    }
    
    /**
     * constructor for task manager who takes in a alias config
     * @param aliasConfig
     */
    public TaskManagerConfig(AliasConfig aliasConfig){
        this.alias = aliasConfig;
    }
    
    /**
     * @return the alias
     */
    public AliasConfig getAlias(){
        return this.alias;
    }
    
    /**
     * @param aliasConfig
     */
    public void setAlias(AliasConfig aliasConfig){
        this.alias = aliasConfig;
    }
    @Override
    public boolean isValid() {
        return true;
    }

}
