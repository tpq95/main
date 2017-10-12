package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

public class TagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags multiple people using the same tag. ";
    public static final String MESSAGE_TAG_PERSONS_SUCCESS = "New tags added";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_DUPLICATE_TAG = "One or more person(s) already has this tag";
    private final Index[] indices;
    private final Set<Tag> tags;
    
    public TagCommand(Index[] indices, Set<Tag> tagList) {
        requireNonNull(indices);
        requireNonNull(tagList);
        this.indices = indices;
        tags = tagList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        for (Index currentIndex : indices) {
            if (currentIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            ReadOnlyPerson personToEdit = lastShownList.get(currentIndex.getZeroBased());

            Set<Tag> oldTags = personToEdit.getTags();
            tags.addAll(oldTags);

            EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
            editPersonDescriptor.setTags(tags);

            Person editedPerson = EditCommand.createEditedPerson(personToEdit, editPersonDescriptor);

            try {
                model.updatePerson(personToEdit, editedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_TAG_PERSONS_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagCommand // instanceof handles nulls
                && tags.equals(((TagCommand) other).tags));
    }

}
