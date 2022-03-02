package ma.octo.dependency.injection.repository.impl;

import ma.octo.dependency.injection.entity.Language;
import ma.octo.dependency.injection.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class LanguageRepositoryImpl implements LanguageRepository {
  private static final List<Language> LANGUAGES;

  static {
    var java = new Language("java", "Java", "James Gosling", "java");
    var cpp = new Language("cpp", "C++", "Bjarne Stroustrup", "cpp");
    var cSharp = new Language("csharp", "C#", "Andres Hejlsberg", "cs");
    var perl = new Language("perl", "Perl", "Larry Wall", "pl");
    var haskell = new Language("haskel", "Haskell", "Simon Peyton", "hs");
    var lua = new Language("lua", "Lua", "Luiz Henrique", "lua");
    var python = new Language("python", "Python", "Guido van Rossum", "py");
    LANGUAGES = List.of(java, cpp, cSharp, perl, haskell, lua, python);
  }

  @Override
  public Optional<Language> findByExtension(final String extension) {
    return LANGUAGES.stream()
        .filter(languageExtensionPredicate(extension))
        .findAny();
  }

  @Override
  public Optional<Language> findById(final String id) {
    return LANGUAGES.stream()
        .filter(languageIdPredicate(id))
        .findAny();
  }

  @Override
  public List<Language> findAll() {
    return LANGUAGES;
  }

  private Predicate<Language> languageExtensionPredicate(final String extension) {
    return language -> Objects.equals(extension, language.getFileExtension());
  }

  private Predicate<Language> languageIdPredicate(final String id) {
    return language -> Objects.equals(id, language.getId());
  }
}
