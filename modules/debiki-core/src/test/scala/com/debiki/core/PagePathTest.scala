/**
 * Copyright (C) 2011-2012 Kaj Magnus Lindberg (born 1979)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.debiki.core


import org.scalatest._
import java.{util => ju}
import com.debiki.core.PostActionDto._


class PagePathTest extends FreeSpec with MustMatchers {

  def parse(urlPath: String): PagePath = PagePath.fromUrlPath("tenantId", urlPath) match {
    case PagePath.Parsed.Bad(error) =>
      fail(s"Error parsing path: $urlPath, error: $error")
    case PagePath.Parsed.Corrected(correctedPath) =>
      fail(s"Error parsing path: `$urlPath', wants to correct it to: $correctedPath")
    case PagePath.Parsed.Good(pagePath) =>
      pagePath
  }

  "PagePath can" - {

    "parse '/'" in {
      val pagePath = parse("/")
      pagePath.folder mustBe "/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe None
    }

    "parse '/folder/'" in {
      val pagePath = parse("/folder/")
      pagePath.folder mustBe "/folder/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe None
    }

    "parse '/so/many/foldes/'" in {
      val pagePath = parse("/so/many/folders/")
      pagePath.folder mustBe "/so/many/folders/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe None
    }

    "parse '/slug'" in {
      val pagePath = parse("/slug")
      pagePath.folder mustBe "/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe None
    }

    "parse '/folder/slug'" in {
      val pagePath = parse("/folder/slug")
      pagePath.folder mustBe "/folder/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe None
    }

    "parse '/so/many/folders/slug'" in {
      val pagePath = parse("/so/many/folders/slug")
      pagePath.folder mustBe "/so/many/folders/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe None
    }

    "parse '/-pageId'" in {
      val pagePath = parse("/-pageId")
      pagePath.folder mustBe "/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe Some("pageId")
    }

    "parse '/folder/-pageId'" in {
      val pagePath = parse("/folder/-pageId")
      pagePath.folder mustBe "/folder/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe Some("pageId")
    }

    "parse '/so/many/folders/-pageId'" in {
      val pagePath = parse("/so/many/folders/-pageId")
      pagePath.folder mustBe "/so/many/folders/"
      pagePath.pageSlug mustBe ""
      pagePath.pageId mustBe Some("pageId")
    }

    "parse '/-pageId-slug'" in {
      val pagePath = parse("/-pageId-slug")
      pagePath.folder mustBe "/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe Some("pageId")
    }

    "parse '/folder/-pageId-slug'" in {
      val pagePath = parse("/folder/-pageId-slug")
      pagePath.folder mustBe "/folder/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe Some("pageId")
    }

    "parse '/so/many/folders/-pageId-slug'" in {
      val pagePath = parse("/so/many/folders/-pageId-slug")
      pagePath.folder mustBe "/so/many/folders/"
      pagePath.pageSlug mustBe "slug"
      pagePath.pageId mustBe Some("pageId")
    }


    "find its parent folder" in {
      val rootFldr: PagePath = PagePath(tenantId = "tenantId", folder = "/",
         pageSlug = "", pageId = None, showId = false)
      val indexPage = rootFldr.copy(pageId = Some("abcd"))
      val idPage = indexPage.copy(showId = true)
      val slugPage = rootFldr.copy(pageSlug = "slug")
      val slugPageWithId = slugPage.copy(pageId = Some("abcd"), showId = true)
      val folder = rootFldr.copy(folder = "/folder/")
      val folderMuuPage = folder.copy(pageSlug = "MuuPage")
      val folderMuuIdPage = folderMuuPage.copy(
                              pageId = Some("muuid"), showId = true)
      val folderSubfolder = folder.copy(folder = "/folder/subfolder/")

      rootFldr.parentFolder mustBe None
      indexPage.parentFolder mustBe Some(rootFldr)
      idPage.parentFolder mustBe Some(rootFldr)
      slugPage.parentFolder mustBe Some(rootFldr)
      slugPageWithId.parentFolder mustBe Some(rootFldr)
      folder.parentFolder mustBe Some(rootFldr)
      folderMuuPage.parentFolder mustBe Some(folder)
      folderMuuIdPage.parentFolder mustBe Some(folder)
      folderSubfolder.parentFolder mustBe Some(folder)
    }

    "find what parent?? for /double-slash//" in {
      // COULD make folder = "/doudle-slash//" work too?
      // Currently that results in "/doudle-slash/" I think.
    }
  }

}

// vim: fdm=marker et ts=2 sw=2 tw=80 fo=tcqwn list

