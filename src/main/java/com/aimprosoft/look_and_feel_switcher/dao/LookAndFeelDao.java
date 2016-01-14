package com.aimprosoft.look_and_feel_switcher.dao;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * crated by m.tkachenko on 19.10.15 10:41
 */
public interface LookAndFeelDao extends CrudRepository<LookAndFeel, Integer> {

    LookAndFeel findById(Integer id);

    @Query("select l.id from LookAndFeel l where l.companyId = ?1")
    List<Integer> getIds(Long companyId);


    @Query("from LookAndFeel l where l.companyId = ?1 and l.colorSchemeId is null and l.type = 'THEME'")
    List<LookAndFeel> getThemes(Long companyId);

    @Query("from LookAndFeel l where l.themeId = ?1 and l.companyId = ?2 and l.colorSchemeId is null and l.type = 'THEME'")
    LookAndFeel findTheme(String themeId, Long companyId);


    @Query("from LookAndFeel l where l.themeId = ?1 and l.colorSchemeId = ?2 and l.companyId = ?3 and l.type = 'COLOR_SCHEME'")
    LookAndFeel findColorScheme(String themeId, String colorSchemeId, Long companyId);

    @Query("from LookAndFeel l where l.themeId=?1 and l.companyId = ?2 and l.colorSchemeId is not null and l.type = 'COLOR_SCHEME'")
    List<LookAndFeel> findColorSchemes(String themeId, Long companyId);

}