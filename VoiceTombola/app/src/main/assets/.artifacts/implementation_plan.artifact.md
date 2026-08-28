# Elongate the Caller Game Board (Tabellone)

The user wants to "lengthen" the game board in the Caller mode. This likely means making the board take up more vertical space and ensuring the numbers are larger and easier to read.

## Proposed Changes

### UI Styles in index.html

#### [MODIFY] [index.html](file:///C:/VoiceTombola/app/src/main/assets/index.html)
- **Body Height:** Change `height: 80vh;` to `height: 100vh;` to utilize the full screen.
- **Tabellone Container:**
    - Remove the fixed `height: 1000px;` which might be causing layout issues.
    - Set `flex: 1;` to allow it to fill all available space between the header/number display and the toolbar.
- **Table (Tab):**
    - Ensure the table has `height: 100%;` to fill the container.
- **Table Cells (TD):**
    - Increase `font-size` slightly if needed (e.g., from `1.2rem` to `1.4rem`).
    - The `table-layout: fixed` and `height: 100%` on the table will naturally "stretch" the rows to fill the elongated container.

## Verification Plan

### Manual Verification
- **Caller Mode:** Open the Caller screen and verify that the board fills the screen height more effectively.
- **Player Mode:** Verify that switching to Player mode doesn't break the layout of the cards (which also rely on the container).
- **Responsiveness:** Check on different aspect ratios if possible.
