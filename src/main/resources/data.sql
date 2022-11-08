DROP ALIAS IF EXISTS TO_UPPERCASE;
CREATE ALIAS TO_UPPERCASE as '
import java.lang.*;
@CODE
String toUpperCase(String parameter) {
    return parameter.toUpperCase();
}
';