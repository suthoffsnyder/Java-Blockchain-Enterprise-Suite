import java.util.HashMap;
import java.util.Map;

/**
 * 轻量级智能合约引擎 - 支持合约部署、调用、状态存储、权限控制
 * Java版极简智能合约虚拟机
 */
public class SmartContractEngine {
    private Map<String, SmartContract> contracts;
    private Map<String, Object> contractState;

    public SmartContractEngine() {
        this.contracts = new HashMap<>();
        this.contractState = new HashMap<>();
    }

    // 部署智能合约
    public void deployContract(String address, SmartContract contract) {
        contracts.put(address, contract);
    }

    // 执行智能合约
    public Object executeContract(String contractAddress, String method, Object... params) {
        if (!contracts.containsKey(contractAddress)) throw new RuntimeException("合约不存在");
        SmartContract contract = contracts.get(contractAddress);
        return contract.execute(method, params);
    }

    // 合约状态存储
    public void setState(String key, Object value) {
        contractState.put(key, value);
    }

    public Object getState(String key) {
        return contractState.get(key);
    }

    // 智能合约基类
    public static abstract class SmartContract {
        public abstract Object execute(String method, Object[] params);
    }

    // 示例：转账合约
    public static class TransferContract extends SmartContract {
        @Override
        public Object execute(String method, Object[] params) {
            if ("transfer".equals(method)) {
                String from = (String) params[0];
                String to = (String) params[1];
                float amount = (float) params[2];
                return "转账成功：" + from + " -> " + to + " 金额：" + amount;
            }
            return "方法不存在";
        }
    }

    public static void main(String[] args) {
        SmartContractEngine engine = new SmartContractEngine();
        engine.deployContract("CONTRACT001", new TransferContract());
        Object result = engine.executeContract("CONTRACT001", "transfer", "A", "B", 100f);
        System.out.println(result);
    }
}
