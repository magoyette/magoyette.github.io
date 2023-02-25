import rss from "@astrojs/rss";
import { getCollection, CollectionEntry } from "astro:content";
import { SITE_TITLE, SITE_DESCRIPTION } from "../consts";

export async function get(context) {
  const articles = await getCollection("articles");
  const notes = await getCollection("notes");
  const pages = [...articles, ...notes].sort(
    (first, second) => first.data.pubDate - second.data.pubDate
  );

  return rss({
    title: SITE_TITLE,
    description: SITE_DESCRIPTION,
    site: context.site,
    items: pages.map((post) => ({
      ...post.data,
      link: `/notes/${post.slug}/`,
    })),
  });
}
