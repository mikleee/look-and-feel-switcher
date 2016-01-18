package com.aimprosoft.look_and_feel_switcher.model.persist;

import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * crated by m.tkachenko on 19.10.15 10:34
 */
@Entity
@Table(name = "ts_look_and_feel")
public class LookAndFeel implements PersistModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LookAndFeelType type;
    @Column(name = "theme_id")
    private String themeId;
    @Column(name = "color_scheme_id")
    private String colorSchemeId;
    @Column(name = "company_id")
    private Long companyId;
    @OneToMany(mappedBy = "lookAndFeel")
    private List<LookAndFeelBinding> bindings;


    @Transient
    private Theme theme;
    @Transient
    private ColorScheme colorScheme;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public LookAndFeelType getType() {
        return type;
    }

    public void setType(LookAndFeelType type) {
        this.type = type;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getColorSchemeId() {
        return colorSchemeId;
    }

    public void setColorSchemeId(String colorSchemeId) {
        this.colorSchemeId = colorSchemeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @JsonIgnore
    public List<LookAndFeelBinding> getBindings() {
        return bindings;
    }

    public void setBindings(List<LookAndFeelBinding> bindings) {
        this.bindings = bindings;
    }

    @JsonIgnore
    public Theme getTheme() {
        if (theme == null) {
            init();
        }
        return theme;
    }

    @JsonIgnore
    public ColorScheme getColorScheme() {
        if (colorScheme == null) {
            init();
        }
        return colorScheme;
    }

    private void init() {
        theme = ThemeLocalServiceUtil.fetchTheme(companyId, themeId);

        if (theme != null && colorSchemeId != null) {
            for (ColorScheme scheme : theme.getColorSchemes()) {
                if (scheme.getColorSchemeId().equals(colorSchemeId)) {
                    colorScheme = scheme;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type=" + type +
                ", themeId='" + themeId + '\'' +
                ", colorSchemeId='" + colorSchemeId + '\'' +
                ", companyId=" + companyId;
    }

    public LookAndFeel() {
    }

    public LookAndFeel(Theme theme, Long companyId) {
        this.themeId = theme.getThemeId();
        this.companyId = companyId;
    }

    public LookAndFeel(Theme theme, ColorScheme colorScheme, Long companyId) {
        this(theme, companyId);
        this.colorSchemeId = colorScheme.getColorSchemeId();
    }


}