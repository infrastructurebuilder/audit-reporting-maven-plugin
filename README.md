



# audit-reporting-maven-plugin
Plugin to execute a series of audits, generate a result artifact, and report the results via the Maven site.

## Examples

Examples can be found underneath the integration test directory `src/it`

### Excel goal

The `excel` goal uses the `poi` library under-the-hood which exhibits characteristics to be aware of:

-  The build may fail from a `poi` exception (`ArrayIndexOutofBounds` and similiar). When this happens, running the build again will often product the expected result.

-  The build may become inefficient in the excel goal with large-scale datasets (e.g. those over 100k or more `AuditResult` entries)

In the future, we will include a `csv` goal instead that can be used for Excel and either retire the excel goal or keep both.
