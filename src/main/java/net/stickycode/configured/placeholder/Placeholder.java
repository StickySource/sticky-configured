package net.stickycode.configured.placeholder;

public interface Placeholder {

  boolean notFound();

  String getKey();

  String replace(String lookup);

}