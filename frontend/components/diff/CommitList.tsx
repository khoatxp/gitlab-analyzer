import React, {useEffect, useState} from 'react';
import {Commit} from "../../interfaces/GitLabCommit";
import DiffItemList, {DiffItem} from "./DiffItemList";

type CommitListProps = {
    commits: Commit[]
    handleSelectCommit: (commit: Commit) => void;
}

const CommitList = ({commits, handleSelectCommit}: CommitListProps) => {
    const [selectedIndex, setSelectedIndex] = useState(-1);
    useEffect(() => {
        // Reset selection when commits change
        setSelectedIndex(-1);
        console.log("CHANGED")
    }, [commits])

    const diffItems = commits.map((commit) => {
        let diffItem: DiffItem = {
            id: commit.id,
            createdAt: commit.created_at,
            authorName: commit.author_name,
            title: commit.title,
        }
        return diffItem;
    });

    const handleSelectDiffItem = (diffItem: DiffItem) => {
        const commit = commits.find((commit) => {
            return commit.id == diffItem.id;
        })
        if (!commit) {return;}
        handleSelectCommit(commit);
    }

    return (
        <DiffItemList
            diffItems={diffItems}
            handleSelectDiffItem={handleSelectDiffItem}
            selectedIndex={selectedIndex}
            setSelectedIndex={setSelectedIndex}
        />
    );
}

export default CommitList;