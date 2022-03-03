package ma.octo.aop.repository.impl;

import ma.octo.aop.db.DbManger;
import ma.octo.aop.entity.Language;
import ma.octo.aop.repository.LanguageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("dev")
public class LanguageRepositoryDevImpl implements LanguageRepository {
    private final DbManger dbManger;

    public LanguageRepositoryDevImpl(DbManger dbManger) {
        this.dbManger = dbManger;
    }

    @Override
    public Optional<Language> findByExtension(String extension) {
        return dbManger.getLanguageByExtension(extension);
    }

    @Override
    public Optional<Language> findById(String id) {
        return dbManger.getLanguageByExtension(id);
    }

    @Override
    public List<Language> findAll() {
        return dbManger.findAllLanguages();
    }
}
