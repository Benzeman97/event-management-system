@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {
    
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return Arrays.stream(params)
            .map(String::valueOf)
            .collect(Collectors.joining("-"));
    }
}
