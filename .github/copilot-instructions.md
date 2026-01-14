When user writes /migration write "Hi! Do you want to invoke the migration tool? This tool is
designed to help you migrate your project." then wait for developer
answer.
If developer answers yes then follow `copilot-migration-guide.md`. Do not get any other file in
context before you start and look at this guide. This guide will show you the progress so you can
decide then what to do.

Any time migration is performed, if through tool or just continuing already in progress, always follow the guide `copilot-migration-guide.md` to see how to communicate with developer and which multi-agent flows to follow.

When user writes /migrate-plan write "Hi! I'll show you the plan for the current migration step."
Then read `.github/migration/[YOUR-APP]-migration-progress.md` to
identify the current step (look for the last completed step and determine the next one). Read
`.github/migration/[YOUR-APP]-migration-guide.md` to get the
details for that step. Present a concise plan showing what will be done in the current/next step
without explanations or implementation details.

When user writes /migrate-explain write "Hi! I'll explain the current migration step in detail."
Then read `.github/migration/[YOUR-APP]-migration-progress.md` to
identify the current step. Read
`.github/migration/[YOUR-APP]-migration-guide.md` and
`.github/migration/instructions/[YOUR-APP]-requirements.md` to understand
the requirements. Provide a detailed explanation of why this step is needed, what it accomplishes,
how it fits into the overall migration, and any important considerations or dependencies.

When user writes /migrate-help write "Hi! I'll explain the overall migration process and available
tools." Then read `.github/migration/copilot-migration-guide.md` and present an
overview of: 1) The migration phases, 2) The key
documents used in migration and their purpose, 3) Available migration tools (/migration,
/migrate-plan, /migrate-explain, /migrate-validate, /migrate-update), 4) Best practices for
successful migration (incremental approach, building after each step, etc.).

When user writes /migrate-validate write "Hi! I'll validate that the current migration step has been
performed correctly." Then read
`.github/migration/migration-progress.md` to identify
what was just completed. Read
`.github/migration/migration-guide.md` to understand
what should have been done. Then verify by: 1) Checking that relevant files exist and contain
expected changes, 2) Looking for any compilation errors, 3) Verifying the build works if
appropriate, 4) Checking that requirements from
`.github/migration/instructions/[YOUR-APP]-requirements.md` are met, 5)
Raising any concerns or issues found, 6) Suggesting corrections if problems are detected. Provide a
summary with either confirmation that the step is correctly completed or specific concerns that need
to be addressed.

When user writes /migrate-update write "Hi! Do you want to invoke the update migration tool? This
tool helps you update the migration guide and progress document when issues or concerns arise during
migration. Please describe the focus area or problem you encountered." then wait for developer
answer.
If developer answers yes or provides a focus area then follow
`.github/migration/copilot-update-migration-guide.md`.

Do not add any comments, you can leave existing but do not add any new comments when generating
code.

Don't use _ in naming of methods.
