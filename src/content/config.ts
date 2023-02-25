import { defineCollection, z } from "astro:content";

const articles = defineCollection({
  schema: z.object({
    title: z.string(),
    description: z.string(),
    pubDate: z
      .string()
      .optional()
      .or(z.date())
      .transform((str) => (str ? new Date(str) : undefined)),
    updatedDate: z
      .string()
      .optional()
      .or(z.date())
      .transform((str) => (str ? new Date(str) : undefined)),
    tags: z.array(z.string()).optional(),
  }),
});

const notes = defineCollection({
  // Type-check frontmatter using a schema
  schema: z.object({
    title: z.string(),
    description: z.string(),
    pubDate: z
      .string()
      .or(z.date())
      .transform((val) => new Date(val)),
    updatedDate: z
      .string()
      .optional()
      .transform((str) => (str ? new Date(str) : undefined)),
    tags: z.array(z.string()),
  }),
});

export const collections = { articles: articles, notes: notes };
