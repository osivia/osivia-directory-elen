package org.osivia.directory.v2;

import org.osivia.portal.api.transaction.ITransactionResource;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * The Class TransactionalResource.
 */
public class TransactionalResource implements ITransactionResource {
    
    
    ContextSourceTransactionManagerDelegate transactionDelegate;
    Object transaction;
    

    public TransactionalResource(ContextSourceTransactionManagerDelegate transactionDelegate) {
        super();
        this.transactionDelegate = transactionDelegate;
        
        TransactionDefinition definition = new TransactionDefinition() {
            @Override
            public boolean isReadOnly() {
                return false;
            }
            
            @Override
            public int getTimeout() {
                return 0;
            }
            
            @Override
            public int getPropagationBehavior() {
                 return TransactionDefinition.PROPAGATION_REQUIRED;
            }
            
            @Override
            public String getName() {
                return "portal";
            }
            
            @Override
            public int getIsolationLevel() {
                return TransactionDefinition.ISOLATION_DEFAULT;

            }
        };
        
        
        transaction = transactionDelegate.doGetTransaction();
        transactionDelegate.doBegin(transaction, definition);
        
        
    }

    @Override
    public Object getInternalTransaction() {
        return transaction;
    }

    @Override
    public void commit() {
        transactionDelegate.doCommit(new DefaultTransactionStatus(transaction, true, true, false, false, null));
        
    }

    @Override
    public void rollback() {
        transactionDelegate.doRollback(new DefaultTransactionStatus(transaction, true, true, false, false, null));
        
    }

}
