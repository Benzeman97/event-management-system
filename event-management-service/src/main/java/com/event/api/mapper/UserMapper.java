
@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    @Mappings({
        @Mapping(source = "firstName", target = "givenName"),
        @Mapping(source = "lastName", target = "familyName"),
        @Mapping(source = "email", target = "emailAddress"),
       @Mapping(target = "createdAt", ignore = true) // Ignore Fields That Don’t Exist
    })
    User toEntity(CreateUserRequest request);

    @Mapping(source = "emailAddress", target = "email")
    UserResponse toResponse(User user);

    User updateEntity(UpdateUserRequest request,
                      @MappingTarget User user);  // @MappingTarget - Update this existing entity, don’t create a new one
}
