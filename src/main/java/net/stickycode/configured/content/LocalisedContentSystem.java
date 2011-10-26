package net.stickycode.configured.content;

import javax.inject.Inject;

import net.stickycode.configured.ConfigurationKeyBuilder;
import net.stickycode.stereotype.PostConfigured;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.ui.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class LocalisedContentSystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private LocalisedElementRepository localisedElements;

  @Inject
  private ConfigurationKeyBuilder keyBuilder;

  @Inject
  LocaleProvider localeProvider;

  @Inject
  private LocalisedContentSource content;

  @PostConfigured
  public void localise() {
    log.info("localising content {}", this);
    for (LocalisedElement element : localisedElements)
      localise(element);
  }

  private void localise(LocalisedElement element) {
    for (LocalisedAttribute attribute : element) {
      log.debug("configuring content {}", attribute);
      String key = keyBuilder.build(element, attribute);
      processElement(attribute, key);
    }
  }

  private void processElement(LocalisedAttribute element, final String key) {
    if (localeProvider.isSingleton()) {
      String value = content.getContent(key, localeProvider.get());
      element.setValue(value);
    }
    else {
      element.setValue(new Content() {

        @Override
        public String get() {
          return content.getContent(key, localeProvider.get());
        }
      });
    }
  }

}
