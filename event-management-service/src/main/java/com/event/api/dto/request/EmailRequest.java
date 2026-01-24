@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest implements Serializable {

    private String to;
    private String subject;
    private String body;
}
