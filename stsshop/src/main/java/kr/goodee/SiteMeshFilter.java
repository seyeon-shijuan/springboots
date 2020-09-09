package kr.goodee;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "/WEB-INF/layout/boardlayout.jsp")
		.addDecoratorPath("/user/*", "/WEB-INF/layout/userlayout.jsp")
		.addExcludedPath("/board/imgupload*")
		.addExcludedPath("/admin/graph*")
		.addExcludedPath("/ajax/*");
		
	}
}
