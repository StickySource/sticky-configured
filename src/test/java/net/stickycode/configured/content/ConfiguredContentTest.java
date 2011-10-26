package net.stickycode.configured.content;

import net.stickycode.stereotype.ui.ConfiguredContent;
import net.stickycode.stereotype.ui.Content;



public class ConfiguredContentTest {

 @ConfiguredContent
 String some;
 
 @ConfiguredContent
 String withDefault = "A default value";
 
 @ConfiguredContent
 Content content;
 
}
