package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.Note;
import com.eris.gitlabanalyzer.model.types.Author;

import java.time.OffsetDateTime;

@lombok.Getter
@lombok.AllArgsConstructor
public class NoteView {
    private final Long id;
    private final String body;
    private final Author author;
    private final OffsetDateTime createdAt;
    private final Long projectId;
    private final boolean own;
    private final Long parentIid;
    private final String parentWebUrl;

    public static NoteView fromNote(Note note) {
        return new NoteView(
                note.getGitLabNoteId(),
                note.getBody(),
                Author.fromGitManagementUser(note.getGitManagementUser()),
                note.getCreatedAt(),
                note.getProjectId(),
                note.isOwn(),
                note.getParentIid(),
                note.getParentWebUrl()
        );
    }
}
