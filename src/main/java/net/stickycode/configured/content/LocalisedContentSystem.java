package net.stickycode.configured.content;

import javax.inject.Inject;

import net.stickycode.configured.ConfigurationKeyBuilder;
import net.stickycode.configured.ConfigurationListener;
import net.stickycode.stereotype.StickyPlugin;
import net.stickycode.stereotype.ui.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyPlugin
public class LocalisedContentSystem
    implements ConfigurationListener {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private LocalisedElementRepository localisedElements;

  @Inject
  private ConfigurationKeyBuilder keyBuilder;

  @Inject
  private LocaleProvider localeProvider;

  @Inject
  private LocalisedContentSource content;

  @Override
  public void configure() {
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

  @Override
  public void resolve() {
  }

  @Override
  public void preConfigure() {
  }

  @Override
  public void postConfigure() {
  }

}
