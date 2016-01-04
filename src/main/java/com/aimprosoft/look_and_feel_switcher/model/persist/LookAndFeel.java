package com.aimprosoft.look_and_feel_switcher.model.persist;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "lfb_look_and_feel")
public class LookAndFeel implements PersistModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "theme_id")
    private String themeId;
    @Column(name = "color_scheme_id")
    private String colorSchemeId;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "shown")
    private Boolean shown = true;
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

    public Boolean getShown() {
        return shown;
    }

    public void setShown(Boolean hidden) {
        this.shown = hidden;
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

        if (colorSchemeId != null) {
            for (ColorScheme scheme : theme.getColorSchemes()) {
                if (scheme.getColorSchemeId().equals(colorSchemeId)) {
                    colorScheme = scheme;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (id != null)
            sb.append("id=").append(id);
        if (themeId != null)
            sb.append(", themeId=").append(themeId);
        if (colorSchemeId != null)
            sb.append(", colorSchemeId=").append(colorSchemeId);
        if (companyId != null)
            sb.append(", companyId=").append(companyId);
        if (shown != null)
            sb.append(", shown=").append(shown);
        return sb.toString();
    }

    public LookAndFeel() {
    }

    public LookAndFeel(Theme theme, Long companyId, boolean shown) {
        this.themeId = theme.getThemeId();
        this.companyId = companyId;
        this.shown = shown;
    }

    public LookAndFeel(Theme theme, ColorScheme colorScheme, Long companyId, boolean shown) {
        this(theme, companyId, shown);
        this.colorSchemeId = colorScheme.getColorSchemeId();
    }

    public void validate() throws ApplicationException {
        if (themeId == null) {
            throw new ApplicationException("Select theme");
        }
        init();

        if (theme == null) {
            throw new ApplicationException("Requested theme is not exist anymore");
        }

        if (theme.getColorSchemes().isEmpty()) {
            if (colorSchemeId != null) {
                throw new ApplicationException("Requested theme does not support any color schemes anymore");
            }
        } else {
            for (ColorScheme scheme : theme.getColorSchemes()) {
                if (scheme.getColorSchemeId().equals(colorSchemeId)) {
                    return;
                }
            }
            throw new ApplicationException("Requested color scheme is not supported anymore");
        }

    }


}