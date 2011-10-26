package net.stickycode.configured.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.stickycode.stereotype.component.StickyRepository;

@StickyRepository
public class InlineLocalisedElementRepository
    implements LocalisedElementRepository {

  private List<LocalisedElement> contents = new ArrayList<LocalisedElement>();
  
  @Override
  public Iterator<LocalisedElement> iterator() {
    return contents.iterator();
  }

  @Override
  public void register(LocalisedElement content) 
      throws DuplicateContentConfigurationException {
    if (contents.contains(content))
      throw new DuplicateContentConfigurationException(content);
    
    contents.add(content);
  }

}
