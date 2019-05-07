/**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
     *
     * @param element the element to add
     * @return <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     * @throws DocumentException when a document isn't open yet, or has been closed
     */
@Override
public boolean add(Element element) throws DocumentException {
    if (writer != null && writer.isPaused()) {
        return false;
    }
    try {
        switch(element.type()) {
            // Information (headers) 
            case Element.HEADER:
                info.addkey(((Meta) element).getName(), ((Meta) element).getContent());
                break;
            case Element.TITLE:
                info.addTitle(((Meta) element).getContent());
                break;
            case Element.SUBJECT:
                info.addSubject(((Meta) element).getContent());
                break;
            case Element.KEYWORDS:
                info.addKeywords(((Meta) element).getContent());
                break;
            case Element.AUTHOR:
                info.addAuthor(((Meta) element).getContent());
                break;
            case Element.CREATOR:
                info.addCreator(((Meta) element).getContent());
                break;
            case Element.PRODUCER:
                // you can not change the name of the producer 
                info.addProducer();
                break;
            case Element.CREATIONDATE:
                // you can not set the creation date, only reset it 
                info.addCreationDate();
                break;
            // content (text) 
            case Element.CHUNK:
                {
                    // if there isn't a current line available, we make one 
                    if (line == null) {
                        carriageReturn();
                    }
                    // we cast the element to a chunk 
                    PdfChunk chunk = new PdfChunk((Chunk) element, anchorAction);
                    // we try to add the chunk to the line, until we succeed 
                    {
                        PdfChunk overflow;
                        while ((overflow = line.add(chunk)) != null) {
                            carriageReturn();
                            chunk = overflow;
                            chunk.trimFirstSpace();
                        }
                    }
                    pageEmpty = false;
                    if (chunk.isAttribute(Chunk.NEWPAGE)) {
                        newPage();
                    }
                    break;
                }
            case Element.ANCHOR:
                {
                    leadingCount++;
                    Anchor anchor = (Anchor) element;
                    String url = anchor.getReference();
                    leading = anchor.getLeading();
                    if (url != null) {
                        anchorAction = new PdfAction(url);
                    }
                    // we process the element 
                    element.process(this);
                    anchorAction = null;
                    leadingCount--;
                    break;
                }
            case Element.ANNOTATION:
                {
                    if (line == null) {
                        carriageReturn();
                    }
                    Annotation annot = (Annotation) element;
                    Rectangle rect = new Rectangle(0, 0);
                    if (line != null)
                        rect = new Rectangle(annot.llx(indentRight() - line.widthLeft()), annot.ury(indentTop() - currentHeight - 20), annot.urx(indentRight() - line.widthLeft() + 20), annot.lly(indentTop() - currentHeight));
                    PdfAnnotation an = PdfAnnotationsImp.convertAnnotation(writer, annot, rect);
                    annotationsImp.addPlainAnnotation(an);
                    pageEmpty = false;
                    break;
                }
            case Element.PHRASE:
                {
                    leadingCount++;
                    // we cast the element to a phrase and set the leading of the document 
                    leading = ((Phrase) element).getLeading();
                    // we process the element 
                    element.process(this);
                    leadingCount--;
                    break;
                }
            case Element.PARAGRAPH:
                {
                    leadingCount++;
                    // we cast the element to a paragraph 
                    Paragraph paragraph = (Paragraph) element;
                    addSpacing(paragraph.getSpacingBefore(), leading, paragraph.getFont());
                    // we adjust the parameters of the document 
                    alignment = paragraph.getAlignment();
                    leading = paragraph.getTotalLeading();
                    carriageReturn();
                    // we don't want to make orphans/widows 
                    if (currentHeight + line.height() + leading > indentTop() - indentBottom()) {
                        newPage();
                    }
                    indentation.indentLeft += paragraph.getIndentationLeft();
                    indentation.indentRight += paragraph.getIndentationRight();
                    carriageReturn();
                    PdfPageEvent pageEvent = writer.getPageEvent();
                    if (pageEvent != null && !isSectionTitle)
                        pageEvent.onParagraph(writer, this, indentTop() - currentHeight);
                    // if a paragraph has to be kept together, we wrap it in a table object 
                    if (paragraph.getKeepTogether()) {
                        carriageReturn();
                        PdfPTable table = new PdfPTable(1);
                        table.setWidthPercentage(100f);
                        PdfPCell cell = new PdfPCell();
                        cell.addElement(paragraph);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setPadding(0);
                        table.addCell(cell);
                        indentation.indentLeft -= paragraph.getIndentationLeft();
                        indentation.indentRight -= paragraph.getIndentationRight();
                        this.add(table);
                        indentation.indentLeft += paragraph.getIndentationLeft();
                        indentation.indentRight += paragraph.getIndentationRight();
                    } else {
                        line.setExtraIndent(paragraph.getFirstLineIndent());
                        element.process(this);
                        carriageReturn();
                        addSpacing(paragraph.getSpacingAfter(), paragraph.getTotalLeading(), paragraph.getFont());
                    }
                    if (pageEvent != null && !isSectionTitle)
                        pageEvent.onParagraphEnd(writer, this, indentTop() - currentHeight);
                    alignment = Element.ALIGN_LEFT;
                    indentation.indentLeft -= paragraph.getIndentationLeft();
                    indentation.indentRight -= paragraph.getIndentationRight();
                    carriageReturn();
                    leadingCount--;
                    break;
                }
            case Element.SECTION:
            case Element.CHAPTER:
                {
                    // Chapters and Sections only differ in their constructor 
                    // so we cast both to a Section 
                    Section section = (Section) element;
                    PdfPageEvent pageEvent = writer.getPageEvent();
                    boolean hasTitle = section.isNotAddedYet() && section.getTitle() != null;
                    // if the section is a chapter, we begin a new page 
                    if (section.isTriggerNewPage()) {
                        newPage();
                    }
                    if (hasTitle) {
                        float fith = indentTop() - currentHeight;
                        int rotation = pageSize.getRotation();
                        if (rotation == 90 || rotation == 180)
                            fith = pageSize.getHeight() - fith;
                        PdfDestination destination = new PdfDestination(PdfDestination.FITH, fith);
                        while (currentOutline.level() >= section.getDepth()) {
                            currentOutline = currentOutline.parent();
                        }
                        PdfOutline outline = new PdfOutline(currentOutline, destination, section.getBookmarkTitle(), section.isBookmarkOpen());
                        currentOutline = outline;
                    }
                    // some values are set 
                    carriageReturn();
                    indentation.sectionIndentLeft += section.getIndentationLeft();
                    indentation.sectionIndentRight += section.getIndentationRight();
                    if (section.isNotAddedYet() && pageEvent != null)
                        if (element.type() == Element.CHAPTER)
                            pageEvent.onChapter(writer, this, indentTop() - currentHeight, section.getTitle());
                        else
                            pageEvent.onSection(writer, this, indentTop() - currentHeight, section.getDepth(), section.getTitle());
                    // the title of the section (if any has to be printed) 
                    if (hasTitle) {
                        isSectionTitle = true;
                        add(section.getTitle());
                        isSectionTitle = false;
                    }
                    indentation.sectionIndentLeft += section.getIndentation();
                    // we process the section 
                    element.process(this);
                    flushLines();
                    // some parameters are set back to normal again 
                    indentation.sectionIndentLeft -= section.getIndentationLeft() + section.getIndentation();
                    indentation.sectionIndentRight -= section.getIndentationRight();
                    if (section.isComplete() && pageEvent != null)
                        if (element.type() == Element.CHAPTER)
                            pageEvent.onChapterEnd(writer, this, indentTop() - currentHeight);
                        else
                            pageEvent.onSectionEnd(writer, this, indentTop() - currentHeight);
                    break;
                }
            case Element.LIST:
                {
                    // we cast the element to a List 
                    List list = (List) element;
                    if (list.isAlignindent()) {
                        list.normalizeIndentation();
                    }
                    // we adjust the document 
                    indentation.listIndentLeft += list.getIndentationLeft();
                    indentation.indentRight += list.getIndentationRight();
                    // we process the items in the list 
                    element.process(this);
                    // some parameters are set back to normal again 
                    indentation.listIndentLeft -= list.getIndentationLeft();
                    indentation.indentRight -= list.getIndentationRight();
                    carriageReturn();
                    break;
                }
            case Element.LISTITEM:
                {
                    leadingCount++;
                    // we cast the element to a ListItem 
                    ListItem listItem = (ListItem) element;
                    addSpacing(listItem.getSpacingBefore(), leading, listItem.getFont());
                    // we adjust the document 
                    alignment = listItem.getAlignment();
                    indentation.listIndentLeft += listItem.getIndentationLeft();
                    indentation.indentRight += listItem.getIndentationRight();
                    leading = listItem.getTotalLeading();
                    carriageReturn();
                    // we prepare the current line to be able to show us the listsymbol 
                    line.setListItem(listItem);
                    // we process the item 
                    element.process(this);
                    addSpacing(listItem.getSpacingAfter(), listItem.getTotalLeading(), listItem.getFont());
                    // if the last line is justified, it should be aligned to the left 
                    if (line.hasToBeJustified()) {
                        line.resetAlignment();
                    }
                    // some parameters are set back to normal again 
                    carriageReturn();
                    indentation.listIndentLeft -= listItem.getIndentationLeft();
                    indentation.indentRight -= listItem.getIndentationRight();
                    leadingCount--;
                    break;
                }
            case Element.RECTANGLE:
                {
                    Rectangle rectangle = (Rectangle) element;
                    graphics.rectangle(rectangle);
                    pageEmpty = false;
                    break;
                }
            case Element.PTABLE:
                {
                    PdfPTable ptable = (PdfPTable) element;
                    if (ptable.size() <= ptable.getHeaderRows())
                        break;
                    //nothing to do 
                    // before every table, we add a new line and flush all lines 
                    ensureNewLine();
                    flushLines();
                    addPTable(ptable);
                    pageEmpty = false;
                    newLine();
                    break;
                }
            case Element.MULTI_COLUMN_TEXT:
                {
                    ensureNewLine();
                    flushLines();
                    MultiColumnText multiText = (MultiColumnText) element;
                    float height = multiText.write(writer.getDirectContent(), this, indentTop() - currentHeight);
                    currentHeight += height;
                    text.moveText(0, -1f * height);
                    pageEmpty = false;
                    break;
                }
            case Element.JPEG:
            case Element.JPEG2000:
            case Element.JBIG2:
            case Element.IMGRAW:
            case Element.IMGTEMPLATE:
                {
                    //carriageReturn(); suggestion by Marc Campforts 
                    add((Image) element);
                    break;
                }
            case Element.YMARK:
                {
                    DrawInterface zh = (DrawInterface) element;
                    zh.draw(graphics, indentLeft(), indentBottom(), indentRight(), indentTop(), indentTop() - currentHeight - (leadingCount > 0 ? leading : 0));
                    pageEmpty = false;
                    break;
                }
            case Element.MARKED:
                {
                    MarkedObject mo;
                    if (element instanceof MarkedSection) {
                        mo = ((MarkedSection) element).getTitle();
                        if (mo != null) {
                            mo.process(this);
                        }
                    }
                    mo = (MarkedObject) element;
                    mo.process(this);
                    break;
                }
            default:
                return false;
        }
        lastElementType = element.type();
        return true;
    } catch (Exception e) {
        throw new DocumentException(e);
    }
}
